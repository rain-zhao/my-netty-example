package protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ProtoDemoServer {

    public static void main(String[] args) throws InterruptedException {
        //config the server
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.channel(NioServerSocketChannel.class)
                    .group(bossGroup,workerGroup)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ProtoDemoServerChannelInitializer());

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
