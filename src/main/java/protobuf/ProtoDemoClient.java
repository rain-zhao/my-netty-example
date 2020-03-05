package protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import protobuf.bean.SearchRequestOuterClass;

public class ProtoDemoClient {

    public static void main(String[] args) throws InterruptedException {
        //config the client
        EventLoopGroup group = new NioEventLoopGroup();
        try{

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ProtoDemoClientChannelInitializer());

            //start the client
            Channel channel = bootstrap.connect("127.0.0.1", 8090).sync().channel();


            //build request and send
            //builder
            SearchRequestOuterClass.SearchRequest.Builder builder = SearchRequestOuterClass.SearchRequest.newBuilder();
            builder.setQuery("select * from t_user where id = 1001").setPageNumber(1).setResultPerPage(3);

            SearchRequestOuterClass.SearchRequest msg = builder.build();
            System.out.println("发送请求："+msg);
            //send obj directly
            channel.writeAndFlush(msg);

            //wait until the connection is closed
            channel.closeFuture().sync();
        }finally {
            //shut down the event loop to terminate all threads
            group.shutdownGracefully();
        }


    }
}
