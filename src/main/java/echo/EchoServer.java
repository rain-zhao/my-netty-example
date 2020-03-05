package echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {

    public static void main(String[] args) throws InterruptedException {
        //config the server
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.channel(NioServerSocketChannel.class)
                    .group(bossGroup,workerGroup)
                    .handler(new LoggingHandler(LogLevel.INFO))

                    //keepalive 参数 两种配置方式
//                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(NioChannelOption.SO_KEEPALIVE, true)

                    //对象池
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)

                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO))
                                    .addLast(new EchoServerHandler());
                        }
                    });

            //start the server
            ChannelFuture future = b.bind(8090).sync();

            //wait until server is closed
            future.channel().closeFuture().sync();


        }finally {
            //shut down the event loop to terminate all threads
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
