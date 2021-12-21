package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DataBufferWrapper;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

import java.nio.charset.Charset;

public class CachedDataBuffer extends DataBufferWrapper {

    private final DataBuffer cachedBuffer;


    public CachedDataBuffer(DataBuffer delegate) {
        super(delegate);
        cachedBuffer = new DefaultDataBufferFactory().allocateBuffer(delegate.readableByteCount());
        cachedBuffer.write(delegate.asByteBuffer().duplicate());
    }


    public String getCachedContent() {
        byte[] bytes = new byte[cachedBuffer.readableByteCount()];
        cachedBuffer.read(bytes);
        DataBufferUtils.release(cachedBuffer);

        return new String(bytes, Charset.defaultCharset());
    }
}