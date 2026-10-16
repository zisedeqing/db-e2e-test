## 1. directory
运行时依赖5个目录，其中每个目录的文件名都是以测试脚本名字为前缀。
### 1.1 sql
测试case目录，目录命名格式如下：
* __file_name: 隐藏文件，会忽略
* test_xxx.sql: 测试文件，已经test_为前缀，.sql为后缀

测试程序会遍历sql目录，按照遍历的目录顺序执行所有测试文件。
### 1.2 output
测试文件的运行结果保存在该目录，每个测试case运行之前会删除该目录对应的结果文件。
该目录的结果是应用result hook后的结果.原始未应用result hook的结果存储在origin目录
### 1.3 expect
测试文件的期望结果保存在该目录，每个测试case运行结束后，会用实际运行结果，与期望结果进行比较，没有差异则表示运行成功。

期望结果文件的创建：
可以先运行一般测试case，检查output里面的实际运行结果是否正确，如果正确，则把实际运行结果copy到expect即可。
### 1.4 diff
如果测试case的时间运行结果与期望结果有差异，则把差异保存在该目录。
### 1.5 origin
存放sql 实际运行的结果, 也就是没有应用result hook的结果

## 2. test case
### 2.1 编写规则
test_xxx.sql文件，支持如下几种语句：
* 单行和多行注释
```sql
-- single comment
// single comment
/* multi line comment */
```
* 指定session的sql语句
```sql
(s1) create table test(a int, b int);
(s2)& insert into test values(1,1);

(s2)<;
(s2)!;
(s1)!;
```
第一条sql: 表示使用session s1 执行create table test(a int, b int)
第二条sql: 表示使用session s2在后台执行insert into test values(1,1)
第三条sql: join语句，表示等待session s2后台执行的sql结束
第四条sql表示关闭session s2
后台执行sql的session，必须使用join语句等待后台sql完成，否则不能使用该session执行下一条SQL。
* 不指定session的sql
```sql
create table test(a int, b int)
```
不指定session，则使用默认session执行，每个测试脚本都会有一个默认session。
* 环境变量定义语句
```
$var_name=var_value
```
定义一个环境变量。可以在shell脚本或者SQL语句中使用。
* shell 命令或者脚本
```sql
!shell command line;
```
执行指定的shell 命令或者shell脚本, 语法规则是以'!' 开头, 以';'结尾
比如:
```sql
!hdfs dfs -put ./test /usr/tmp/;
```
对于shell脚本,可以使用的环境变量:
1. impalad进程启动时配置的环境变量
2. 实现的固定的环境变量
  IMPALADS: impalad的信息,格式为: host:mysql_port:hs2_port:webserver_port
  WORK_DIR: 测试程序的工作路径
  其他的可以看代码:ConcurrencyTests.setEnvironment
3. 用户自定义的环境变量
  可以使用参数-e 或者 --environment来自定义环境变量
4. env_file 中定义的环境变量
5. 使用环境变量定义语句定义的环境变量
* hook, 一种特殊的单行注释, 用于对语句或者语句执行的结果做处理, 详细说明可以参考hook这节的说明

* annotation, 测试case注解，用于统计单个文件内的test case数量
语法如下：
```
@beforeTest
@test{testname}
@afterTest
```
1. beforeTest, 标记这个注解之后的SQL为初始化SQL。即所有test case 通用的一些操作。
2. afterTest, 用于清理环境，比如删除创建的表等。
3. test{testname}, 测试case，标记这个注解之后的SQL组成了当前测试用例。
示例如下：
```sql
-- match(xxx):xxx
-- replace:yyy

@beforeTest
create database anno;

@testCreateTable
create table anno.t(a int);

@testDropTable
drop table anno.t;

@afterTest
drop database casecade;
```
或者:
```sql
-- match(xxx):xxx
-- replace:yyy

@testCase1
select 1;

@testCase2
select 2;
```
其中beforeTest和afterTest可以省略，单必须成对出现。并且如果有before test注解，则必须做为整个测试文件的第一个有效语句。

为了兼容旧版本格式，也可以不加任何测试注解。但是建议新加的测试都使用新的方式，以方便统计。
### 2.2 运行原理
测试程序，读取SQL文件，按照SQL在文件内的顺序依次执行，所以这些SQL在主线程内是顺序执行的，
遇到后台执行的SQL，则开启一个新的线程执行。

一个session绑定一个connection，所以同一个session的SQL也是串行执行的。

### 2.3 statement 语法规则
具体规则可以查看statement.g4语法文件.
1. session id
```
(session_id)
(session_id:node_id)
```
session_id用小括号包裹, 以字母开头,可以包含下划线 数字和字母.
node_id:数字，从0开始，表示session链接哪个impala node
2. sql
   可以匹配任意sql语句,不同之处是如果sql中包含分号';',则需要使用反斜杠进行转移.
   sql 必须以';'作为结束符.

3. 特殊符号
   &: 表示sql需要后台执行
   <: 表示等待后台执行的session
   !: 表示关闭session
4. shell command 使用'!'开头, 以';'作为语句的结束符

### 2.4 空行
sql 脚本可以写入空行, 空行除了以下情况外,可以出现在任何地方:
1. 不能出现在replace hook的 match 和 replace规则之间
2. 不能出现在ignore hook的下一行

空行中的空格不参与结果集对比.
### 2.5 hook
#### 2.5.1 hook 类型
1. result hook, 只对statement执行的结果生效
   1. delete hook: 把匹配的行删掉
   ```
   -- delete[(hook name)]:正则表达式
   ```
   
   2. ignore hook: diff时忽略下一条SQL的结果
   ```
   -- ignore result
   ```
   3. replace hook: 把正则表达式匹配的结果,全部替换为目标字符串
   ```
   -- match[(hook name)]: 正则表达式
   -- replace: 替换的目标字符串
   ```
   4. extract hook: 只输出正则表达式匹配的内容
   ```
   -- extract[(hook name)]: 正则表达式
   ```
2. statement hook, 针对statement的hook, 在statement执行之前生效
   1. evalenv hook: 计算statement中的正则表达式匹配的所有环境变量
   ```
   -- evalenv[(hook name)]: 正则表达式
   ```
3. combine hook, 一种特殊的hook,可以把多个命名hook组合在一起,同时起作用
    ```
   -- hooks[(hook name)]: hook_name1,...
    ```
   其中hook name 是可选的. 应用顺序是按照定义的顺序依次应用.
   也可以配合组合hook结束标记一起使用,表示对范围内的全部语句生效,如下:
    ```sql
    -- hooks[(hook name)]: hook_name1,...
    sql1
    sql2
    -- hooks
    ```
    这表示sql1和sql2执行的结果都会应用组合hook
#### 2.5.2 hook的用处
result hook主要是为了处理sql执行结果中不确定的字符，比如show files in table, 输出如下:
```text
+----------------------------------------------------------+------+-----------+
| Path                                                     | Size | Partition |
+----------------------------------------------------------+------+-----------+
| hdfs://127.0.0.1/usr/cdw42/test/ds_1_1_646db710/0_0.parq | 584B |           |
| hdfs://127.0.0.1/usr/cdw42/test/ds_2_2_646db714/0_0.parq | 584B |           |
+----------------------------------------------------------+------+-----------+
```
这里的127.0.0.1/usr/cdw42 以及DataSegment的名字都是不确定的，每个人运行都可能会不一样。
这样可以定义一个正则表达式，把这些不确定的字符替换为固定字符，这样就不会影响结果对比了。
比如对于DataSegment的命名可以定义如下的hook来处理:
```text
-- match:ds_[0-9A-Fa-f]+_[0-9A-Fa-f]+_[0-9A-Fa-f]+
-- replace:ds_xxx_xxx_xxx
```
一个测试脚本可以定义多个hook，按照定义的顺序依次应用。

statement hook主要是用于处理要执行的语句,比如语句中包含环境变量,则可以在执行语句之前先计算环境变量.
#### 2.5.3 命名和匿名hook
1. 匿名hook, 没有名字,定义后立即生效
2. 命名hook, 
   1) 只是声明,只有再定义匿名的combine hook引用该命名hook时才会生效
   2) 命名hook只允许定义在hook_file或者测试脚本头部. 
   3) 命名hook是为了复用hook定义,而支持的一个功能, 测试程序可以支持定义全局命名hook,然后在测试脚本中引用, 默认在工作路径下,文件名为:hook_file 
   4) 如果hook的名字相同, 则文件级的命名hook会覆盖全局的命名hook.

#### 2.5.4 作用域
1. 单条语句
   在正常的语句之前定义的hook, 都是只对该语句有效.
2. 块级
   只有combine hook才会有块级作用域.需要显示的指定块的范围,即通过
```sql
-- hooks[(hook name)]: hook_name1,...
```
指定作用域的开始, 通过
```sql
-- hooks
```
指定作用域的结束
3. 文件级
   定义在文件开头的匿名result hook是文件级的,会对文件的所有语句有效
   定义在文件开头的命名result hook只是定义,必须使用 combine hook引用后, 才会有效.
   ignore hook 不支持文件级
#### 2.5.5 执行顺序
   按照hook定义的顺序依次执行, 先定义的先执行. 注意这里说的是定义的顺序,而不是声明的顺序.

#### 2.5.6 环境变量
测试用例支持环境变量, 有2个地方可以使用:
1. shell 命令的执行
2. sql 语句中可以使用环境变量
   配合evalenv hook, 在执行sql之前可以先计算环境变量的值,比如:
   ```
   -- evalenv
   select '${TEST_VAR}';
   ```
   如果环境变量TEST_VAR的值是10,那么执行的sql就是: select '10'

自定义环境变量:
1. 可以使用env_file定义全局的环境变量.
2. ConcurrencyTests的 -e 参数
3. 环境变量定义语句

环境变量优先级,从高到底:
1. 环境变量定义语句定义的环境变量
2. ConcurrencyTests的 -e 参数指定的环境变量
2. env_file 定义的环境变量
3. 系统环境变量
### 2.6 connection options
用于配置执行SQL时,连接到impala的连接参数.
ConcurrencyTests使用JDBC连接impala,并且每个session维护一个独立的连接.
每个连接时在session第一次执行SQL或者session 主动退出后第一次执行SQL时创建,在session活跃期间
都会始终使用这个connection.因此连接参数只有在session创建connection时才有用.

使用如下方式配置session的连接参数:
```
-- connection_options:key=value&key=value&...
```
connection_options可以定义在sql文件的任意位置, 在connection_options定义之后的第一个需要
创建connection的session使用该配置创建连接.
比如:
```sql
(s1) select 1;
-- connection_options:user=user1&password=123456
(s1) select2;
(s2) select 's2';

(s3) select 's3';
```
上面只有session s2 会使用connection_options定义的参数创建连接.
s1和s3都使用默认的配置参数.

## 3. ConcurrencyTests
```text
usage: ConcurrencyTests [-1] [-2] [-3] [-4 <arg>] [-D <arg>] [-d <arg>] [-e <arg>] [-E] [-h] [-i <arg>] [-o
       <arg>] [-p <arg>] [-S] [-t <arg>] [-u <arg>]
 -1,--trace                     enable log trace
 -2,--debug                     enable log debug
 -3,--progress                  print progress for each script
 -4,--progress-interval <arg>   print progress interval, default is 10s
 -D,--database <arg>            database name
 -d,--work-dir <arg>            [Required] workspace directory
 -e,--env <arg>                 custom environment variables, format: name=value
 -E,--exit-on-failed            exit test if found any failed test case, default is false
 -h,--help                      print help info
 -i,--impalad <arg>             [Required] impalad info, format is host:mysql-port:hs2-port:webserver-port,...
 -o,--jdbc-options <arg>        options of jdbc connect
 -p,--password <arg>            user password
 -S,--skip-on-error             skip error test case, default is false
 -t,--test-case <arg>           only run the given test case
 -u,--user <arg>                user name
 -f,--file-of-test <arg>        test case file, default is all files in the given directory
```
示例:
```shell
java -cp ./*:./concurrency-test-1.0-SNAPSHOT-jar-with-dependencies.jar  ConcurrencyTests \
    -i 9.135.1.31:33060:25000,9.135.1.31:33061:25001\
    -b ${IMPALA_HOME}/tests/concurrency-test
```

## 4.TODO
1. 支持sql在指定的impalad上执行