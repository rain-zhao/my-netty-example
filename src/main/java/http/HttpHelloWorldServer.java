package http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HttpHelloWorldServer {
    public static void main(String[] args) throws InterruptedException {

        //config the server
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,group)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer(){

                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpServerCodec())
                                    .addLast(new HttpServerExpectContinueHandler())
                                    .addLast(new HttpHelloWorldServerHandler());
                        }
                    });

            //start the server
            ChannelFuture future = b.bind(8090).sync();

            System.out.println("open you browser and navigate to 127.0.0.1:8090");

            //wait until server is closed
            future.channel().closeFuture().sync();


        }finally {
            bossGroup.shutdownGracefully();
            group.shutdownGracefully();
        }

    }
}
