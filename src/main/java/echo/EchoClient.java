package echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoClient {

    public static void main(String[] args) throws InterruptedException {
        //config the client
        EventLoopGroup group = new NioEventLoopGroup();
        try{

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO))
                                    .addLast(new EchoClientHandler());

                        }
                    });

            //start the client
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8090).sync();
            //wait until the connection is closed
            future.channel().closeFuture().sync();
        }finally {
            //shut down the event loop to terminate all threads
            group.shutdownGracefully();
        }


    }
}
