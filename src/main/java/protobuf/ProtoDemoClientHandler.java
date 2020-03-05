package protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protobuf.bean.UserOuterClass.User;

public class ProtoDemoClientHandler extends SimpleChannelInboundHandler<User> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, User msg) throws Exception {
        System.out.println("请求已收到响应："+msg.toString());
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //关闭channel
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
