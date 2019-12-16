# SpringBoot整合Redis

## redis持久化方式

Redis为持久化提供了两种方式：

- RDB：在指定的时间间隔能对你的数据进行快照存储。
- AOF：记录每次对服务器写的操作,当服务器重启的时候会重新执行这些命令来恢复原始的数据。

### RDB(Redis DataBase)

**RDB 的优势**

- RDB 是一个非常紧凑的文件,它保存了某个时间点的数据集,非常适用于数据集的备份,比如你可以在每个小时报保存一下过去24小时内的数据,同时每天保存过去30天的数据,这样即使出了问题你也可以根据需求恢复到不同版本的数据集。
- RDB 是一个紧凑的单一文件,很方便传送到另一个远端数据中心，非常适用于灾难恢复。
- RDB 在保存 RDB 文件时父进程唯一需要做的就是 fork 出一个子进程,接下来的工作全部由子进程来做，父进程不需要再做其他 IO 操作，所以 RDB 持久化方式可以最大化 Redis 的性能。
- 与AOF相比,在恢复大的数据集的时候，RDB 方式会更快一些。

当 Redis 需要保存 `dump.rdb` 文件时， 服务器执行以下操作:

- Redis 调用forks. 同时拥有父进程和子进程。
- 子进程将数据集写入到一个临时 RDB 文件中。
- 当子进程完成对新 RDB 文件的写入时，Redis 用新 RDB 文件替换原来的 RDB 文件，并删除旧的 RDB 文件。

**RDB的劣势**

- 数据的完整性和一致性不高，因为RDB可能在最后一次备份时宕机了。
- 备份时占用内存，因为Redis 在备份时会独立创建一个子进程，将数据写入到一个临时文件（此时内存中的数据是原来的两倍哦），最后再将临时文件替换之前的备份文件。
  所以Redis 的持久化和数据的恢复要选择在夜深人静的时候执行是比较合理的。

**RDB的配置**

打开 redis.conf 文件，找到 SNAPSHOTTING 对应内容

``` 
# 时间策略
save 900 1
save 300 10
save 60 10000
# 文件名称
dbfilename dump.rdb
# 文件保存路径
dir /home/work/app/redis/data/
# 如果持久化出错，主进程是否停止写入
stop-writes-on-bgsave-error yes
# 是否压缩
rdbcompression yes
# 导入时是否检查
rdbchecksum yes
```

配置其实非常简单，这里说一下持久化的时间策略具体是什么意思。

- `save 900 1` 表示900s内如果有1条是写入命令，就触发产生一次快照，可以理解为就进行一次备份
- `save 300 10` 表示300s内有10条写入，就产生快照

下面的类似，那么为什么需要配置这么多条规则呢？因为Redis每个时段的读写请求肯定不是均衡的，为了平衡性能与数据安全，我们可以自由定制什么情况下触发备份。所以这里就是根据自身Redis写入情况来进行合理配置。

`stop-writes-on-bgsave-error yes` 这个配置也是非常重要的一项配置，这是当备份进程出错时，主进程就停止接受新的写入操作，是为了保护持久化的数据一致性问题。**如果自己的业务有完善的监控系统，可以禁止此项配置，** 否则请开启。

关于压缩的配置 `rdbcompression yes` ，建议没有必要开启，毕竟Redis本身就属于CPU密集型服务器，再开启压缩会带来更多的CPU消耗，相比硬盘成本，CPU更值钱。

当然如果你想要禁用RDB配置，也是非常容易的，只需要在save的最后一行写上：`save ""`

**通过RDB文件恢复数据**

将dump.rdb 文件拷贝到redis的安装目录的bin目录下，重启redis服务即可。在实际开发中，一般会考虑到物理机硬盘损坏情况，选择备份dump.rdb 。可以从下面的操作演示中可以体会到。



### AOF(Append Only File)

**AOF 的优点** 

- 你可以使用不同的 fsync 策略：无 fsync、每秒 fsync 、每次写的时候 fsync .使用默认的每秒 fsync 策略, Redis 的性能依然很好( fsync 是由后台线程进行处理的,主线程会尽力处理客户端请求),一旦出现故障，你最多丢失1秒的数据。
- AOF文件是一个只进行追加的日志文件,所以不需要写入seek,即使由于某些原因(磁盘空间已满，写的过程中宕机等等)未执行完整的写入命令,你也也可使用redis-check-aof工具修复这些问题。
- Redis 可以在 AOF 文件体积变得过大时，自动地在后台对 AOF 进行重写： 重写后的新 AOF 文件包含了恢复当前数据集所需的最小命令集合。 整个重写操作是绝对安全的，因为 Redis 在创建新 AOF 文件的过程中，会继续将命令追加到现有的 AOF 文件里面，即使重写过程中发生停机，现有的 AOF 文件也不会丢失。 而一旦新 AOF 文件创建完毕，Redis 就会从旧 AOF 文件切换到新 AOF 文件，并开始对新 AOF 文件进行追加操作。
- AOF 文件有序地保存了对数据库执行的所有写入操作， 这些写入操作以 Redis 协议的格式保存， 因此 AOF 文件的内容非常容易被人读懂， 对文件进行分析（`parse`）也很轻松。 导出（`export`） AOF 文件也非常简单： 举个例子， 如果你不小心执行了 FLUSHALL 命令， 但只要 AOF 文件未被重写， 那么只要停止服务器， 移除 AOF 文件末尾的 FLUSHALL 命令， 并重启 Redis ， 就可以将数据集恢复到 FLUSHALL 执行之前的状态。

**AOF的缺点**

- 因为AOF记录的内容多，文件会越来越大，数据恢复也会越来越慢。
- 数据恢复较RDB较慢

**AOF的配置**

``` 
# 是否开启aof
appendonly yes
# 文件名称
appendfilename "appendonly.aof"
# 同步方式
appendfsync everysec
# aof重写期间是否同步
no-appendfsync-on-rewrite no
# 重写触发配置
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
# 加载aof时如果有错如何处理
aof-load-truncated yes
# 文件重写策略
aof-rewrite-incremental-fsync yes
```

还是重点解释一些关键的配置：

`appendfsync everysec` 它其实有三种模式:

- always：把每个写命令都立即同步到aof，很慢，但是很安全
- everysec：每秒同步一次，是折中方案
- no：redis不处理交给OS来处理，非常快，但是也最不安全

一般情况下都采用 **everysec** 配置，这样可以兼顾速度与安全，最多损失1s的数据。

`aof-load-truncated yes` 如果该配置启用，在加载时发现aof尾部不正确是，会向客户端写入一个log，但是会继续执行，如果设置为 `no` ，发现错误就会停止，必须修复后才能重新加载。

**根据AOF文件恢复数据**

正常情况下，将appendonly.aof 文件拷贝到redis的安装目录的bin目录下，重启redis服务即可。但在实际开发中，可能因为某些原因导致appendonly.aof 文件格式异常，从而导致数据还原失败，可以通过命令redis-check-aof --fix appendonly.aof 进行修复 。从下面的操作演示中体会。

## SpringBoot整合Redis

