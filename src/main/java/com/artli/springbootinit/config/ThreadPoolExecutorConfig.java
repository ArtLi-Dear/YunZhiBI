package com.artli.springbootinit.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @Configuration 是 Spring 框架中的一个注解，
 * 它用于类级别，表明这个类是一个配置类，
 * 其目的是允许在应用程序上下文中定义额外的 bean。
 */
@Configuration
public class ThreadPoolExecutorConfig {

/**
 * 线程池参数
 * 根据需求确定参数
 * 第一个参数 corePoolSize (核心线程数)。
 * 这些线程就好比是公司的正式员工，他们在正常情况下都是随时待命处理任务的。如何去设定这个参数呢？
 * 比如，如果我们的 AI 服务只允许四个任务同时进行，那么我们的核心线程数应该就被设置为四。
 *
 * • 第二个参数 maximumPoolSize (最大线程数)。
 * 在极限情况下我们的系统或线程池能有多少个线程在工作。就算任务再多，你最多也只能雇佣这么多的人，因为你需要考虑成本和资源的问题。
 * 假设 AI 服务最多只允许四个任务同时执行，那么最大线程数应当设置为四。
 *
 * • 第三个参数 keepAliveTime (空闲线程存活时间)。
 * 这个参数决定了当任务少的时候，临时雇佣的线程会等待多久才会被剔除。这个参数的设定是为了释放无用的线程资源。
 * 你可以理解为，多久之后会“解雇”没有任务做的临时工。
 *
 * • 第四个参数 TimeUnit (空闲线程存活时间的单位)。将 keepAliveTime 和 TimeUnit 组合在一起，
 * 就能指定一个具体的时间，比如说分钟、秒等等。
 *
 * • 第五个参数 workQueue (工作队列)，也就是任务队列。这个队列存储所有等待执行的任务。
 * 也可以叫它阻塞队列，因为线程需要按顺序从队列中取出任务来执行。这个队列的长度一定要设定，
 * 因为无限长度的队列会消耗大量的系统资源。
 *
 * • 第六个参数 threadFactory (线程工厂)。
 * 它负责控制每个线程的生成，就像一个管理员，负责招聘、管理员工，比如设定员工的名字、工资，或者其他属性。
 *
 * • 第七个参数 RejectedExecutionHandler (拒绝策略)。
 * 当任务队列已满的时候，我们应该怎么处理新来的任务？是抛出异常，还是使用其他策略？
 * 比如说，我们可以设定任务的优先级，会员的任务优先级更高。如果你的公司或者产品中有会员业务，
 * 或者有一些重要的业务需要保证不被打扰，你可以考虑定义两个线程池或者两个任务队列，一个用于处理VIP任务，一个用于处理普通任务
 */
//    public ThreadPoolExecutor (int corePoolSize,
//                               int maximumPoolSize,
//                               long keepAliveTime,
//                               TimeUnit unit,
//                               BlockingQueue<Runnable> workQueue,
//                               ThreadFactory threadFactory,
//                               RejectedExecutionHandler handler){
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
       ThreadFactory factory = new ThreadFactory() {
            private int count = 1;
            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("线程" + count);
                count++;
                return thread;
            }
        };

            ThreadPoolExecutor executor = new ThreadPoolExecutor(2,4,100,TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(4),factory);
            return executor;

    }


}
