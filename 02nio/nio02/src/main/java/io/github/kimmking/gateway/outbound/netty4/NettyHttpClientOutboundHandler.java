package io.github.kimmking.gateway.outbound.netty4;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

public class NettyHttpClientOutboundHandler extends ChannelInboundHandlerAdapter {

    private HttpRequest request;

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;

            String uri = request.uri();
            System.out.println("Uri:" + uri);
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(msg.toString().getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().set(CONTENT_LENGTH,
                    response.content().readableBytes());
            response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);

            ctx.write(response);
            ctx.flush();
        }

    }
}