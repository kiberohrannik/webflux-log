package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import org.springframework.core.io.buffer.*;

import java.nio.charset.Charset;

public class CachedDataBuffer extends DataBufferWrapper {

    private final DataBuffer cachedBuffer;

    private final DataBuffer delegate;


    public CachedDataBuffer(DataBuffer delegate) {
        super(delegate);
        cachedBuffer = delegate.factory().allocateBuffer(delegate.readableByteCount());
        cachedBuffer.write(delegate.asByteBuffer().duplicate());

        this.delegate = delegate;
    }


    public String getCachedContent() {
        byte[] bytes = new byte[cachedBuffer.readableByteCount()];
        cachedBuffer.read(bytes);
        DataBufferUtils.release(cachedBuffer);

        return new String(bytes, Charset.defaultCharset());
    }
}