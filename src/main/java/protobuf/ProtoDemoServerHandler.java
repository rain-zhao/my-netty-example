package protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import protobuf.bean.SearchRequestOuterClass.SearchRequest;
import protobuf.bean.UserOuterClass;

public class ProtoDemoServerHandler extends SimpleChannelInboundHandler<SearchRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SearchRequest msg) throws Exception {
        System.out.println("请求已收到："+msg.toString());

        //build response
        UserOuterClass.User.Builder builder = UserOuterClass.User.newBuilder();
        builder.setId(1001).setName("rain.zhao").setSex("male");
        UserOuterClass.User resp = builder.build();

        //send resp
        System.out.println("发送响应："+resp);
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
