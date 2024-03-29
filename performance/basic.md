#### 1. 公式一: RT = Thread CPU Time + Thread Wait Time

RT（Response Time）可以简单的理解为系统从输入到输出的时间间隔，系统可以指一个网站或者一个其他类型的软件应用，也可以指某个设备，比如说手机，手机界面也有响应时间。所以RT是一个比较广泛的概念，不过在接下来的场景，RT都将会特指我们的互联网应用，对于服务器端RT来说，它的含义是指从服务器接受到请求到该请求的响应的全部数据被发往客户端。对于客户端RT来说，它的含义是指从客户端（比如说浏览器）发起请求到客户端（比如说浏览器）接受到该请求的响应的全部数据的时间间隔。需要注意的是服务器端的RT+网络开销约等于客户端的RT。也就是说，一个差的网络环境可以造成两个RT悬殊的差距（比如说从俄罗斯到美国，RTT远大于国内网络环境）。
客户端的RT直接影响着用户体验，要降低客户端RT从而提升用户体验，我们必须考虑两点，一个是服务器端的RT，第二个是网络。对于网络来讲，常见的优化方式有CDN，ADN，专线，这些都适用于不同的场景。
而对于服务器端RT这个命题来说，主要就是看我们服务器端的做法了，从公式中我们可以看到，要降低RT，就是需要降低Thread CPU Time或者Thread Wait Time。所以接下来我们会着重讨论服务器RT，Thread CPU Time，Thread Wait Time相关知识。
在后面的文章中，我将用CPU Time替代Thread CPU Time，用Wait Time替代Thread Wait Time

#### 2. 单线程QPS
在上节中，我们简单的定义了RT由两部分组成，一个是CPU Time，一个是Wait Time。如果系统里只有一个线程，或者一个进程（该进程中只有一个线程），那么最大的QPS是多少呢，1000ms/RT（RT的单位也是ms），假设RT是199ms（CPU Time为19ms，Wait Time是180ms），那么1000ms以内，系统可以接受的最大请求就是1000ms/(19ms+180ms)=5.025。

所以单线程的QPS变成了

单线程QPS=1000ms/RT

#### 3. 最佳线程数
要回答上面这个问题，我们还是从举例开始，延续上面这个例子（CPU Time为19ms，Wait Time是180ms），假设CPU的核心数是1。

假设只有一条线程，这个线程在执行某个请求的时候，其实CPU真正花在这条线程上的时间就是CPU Time，可以看做是19ms，那么在整个RT生命周期中，还有180ms的Wait Time，CPU在做什么？理想情况下，抛开系统层面的问题，我们可以认为CPU在这180ms里没做啥，至少对业务来说没做啥。整整180ms，CPU在那里喝茶，什么事都不干，我很为它下半年的KPI担忧啊。

一核的情况
由于每个请求过来，CPU只需要工作19ms，所以在180ms的时间内，我们可以认为系统还可以额外接受180ms/19ms=9个请求。由于刚刚讲到在同步模型中，一个请求需要一条线程来处理，所以，我们就需要额外的9条线程来处理这些请求。这样，总的线程数其实就是(180ms+20ms)/20ms=10条，这多线程之后CPU Time从19ms变成了20ms，这1ms代表多线程之后，线程上下文切换，GC等带来额外开销等等（如果是JVM），这里的1ms代表一个概数，你也可以把它看做是n。

两核的情况
一个核下可以有10条线程，那么2个核呢，理想情况下，我们可以认为最佳线程数为
2*(180ms+20ms)/20ms=20条

CPU利用率
上面举的例子都是CPU在满载情况下的例子，那么如果由于某个瓶颈，导致CPU利用率得不到有效利用，比如说两核的CPU，因为某个资源，只能各自使用一般的效能，这样总的CPU利用率变成了50%，在这样的情况下，我们的最佳线程数应该是
50%*2*(180ms+20ms)/20ms=10条

根据上面的分析，我们的最佳线程数公式就有了眉目了：

最佳线程数=(RT/CPU Time) * CPU cores * CPU利用率

当然，这个最佳线程数公式不是荣华胡诌出来的，在一些权威著作上都有论述，所以接下来，我们假设这个最佳线程数的公式是正确的。

#### 4. 最大QPS公式推导
假设我们知道了最佳线程数，我们也知道了每条线程的qps，那么线程数乘以每条线程的qps即是这台机器在最佳线程数下的QPS了。所以我们可以得到下面的推算过程：
最大QPS = 最佳线程数 * 单线程QPS = (1000ms / cpu time) * cpu核数 * cpu利用率
公式中，决定QPS的是CPU Time和CPU核数，还有CPU利用率。CPU核数是由硬件决定的，我们很难操纵，但是CPU Time和CPU利用率是跟我们的代码息息相关的。

虽然宏观上是正确的，但是推算的过程中还是有点小小的不完美的，因为多线程下的CPU Time(比如高并发下的GC次数增加消耗更多的CPU Time，线程上下文切换等等)和单线程下的CPU Time是不一样的，所以会导致推算出来的QPS和实际的QPS有点误差。

尤其是在同步模型下相同业务逻辑中单线程时的CPU Time肯定会比大量多线程时的CPU Time要小，但是到了异步模型，切换的开销会变小，后面将会详细描述。

既然决定QPS的是CPU Time和CPU核心数，那么这两个因子又是由什么决定的呢？

#### 5. 为啥异步模型需要的线程数少
为啥同步模型下需要那么多线程，异步模型下只需要几条线程呢？这主要归功于非阻塞式IO。在同步模型下，线程必须阻塞在IO上面，这个阻塞在IO上的时间是要计入线程的Wait Time的，所以当我们把同步模型转化为异步模型之后，业务线程在IO上阻塞的时间大大减少，根据最佳线程数公式,Wait Time逼近0之后,公式变为:CPU cores * CPU 利用率

#### 6. 总结

QPS = (1000ms / cpu time) * cpu核数 * cpu利用率
QPS是由CPU Time和CPU利用率，CPU核数决定的
最佳线程数是由CPU Time和WaitTime以及CPU核数，CPU利用率决定的
多线程下CPU Time的增加以及admahl定律中影响加速比的因素
CPU Time是由数据结构和算法决定的
CPU 利用率是由架构和串行，编程模型和系统中有无其他瓶颈相关的
要做性能优化，必须考察CPU Time降低的百分比和CPU利用率提高的百分比
性能优化同时要考虑串行和并行的比例
处理某个业务的最佳线程数存在一个临界点，超过这个临界点的线程数，QPS会有下降的现象，RT也会明显增加
对编程模型的选择，同步模型还是异步模型，您得根据您的应用特征仔细斟酌