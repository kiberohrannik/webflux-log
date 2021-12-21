package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import lombok.Getter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferWrapper;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

public class CachedDataBuffer extends DataBufferWrapper {

    @Getter
    private final DataBuffer cachedBuffer;


    public CachedDataBuffer(DataBuffer delegate) {
        super(delegate);
        cachedBuffer = new DefaultDataBufferFactory().allocateBuffer(delegate.readableByteCount());
        cachedBuffer.write(delegate.asByteBuffer().duplicate());
    }
}
