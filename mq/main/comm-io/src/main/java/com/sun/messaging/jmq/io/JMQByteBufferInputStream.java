/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2000-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

/*
 * @(#)JMQByteBufferInputStream.java	1.3 06/27/07
 */ 

package com.sun.messaging.jmq.io;

import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A <code>JMQByteBufferInputStream</code> contains
 * an internal ByteBuffer that contains bytes that
 * may be read from the stream.
 */
public
class JMQByteBufferInputStream extends InputStream {

    /**
     * A flag that is set to true when this stream is closed.
     */
    //private boolean isClosed = false;

    /**
     * The ByteBuffer holding data
     */
    protected ByteBuffer buf;

    /**
     * Creates a <code>ByteBufferInputStream</code>
     * so that it  uses <code>ByteBuffer</code> as its
     * buffer.
     * The ByteBuffer is not copied, sliced or duplicated.
     * Reads will start at the current position of the byte buffer.
     *
     * @param   buf   the input buffer.
     */
    public JMQByteBufferInputStream(ByteBuffer buf) {
        this.buf = buf;
    }

    /**
     * Reads the next byte of data from this input stream. The value 
     * byte is returned as an <code>int</code> in the range 
     * <code>0</code> to <code>255</code>. If no byte is available 
     * because the end of the stream has been reached, the value 
     * <code>-1</code> is returned. 
     * <p>
     * This <code>read</code> method 
     * cannot block. 
     * <P>
     * The read will update the backing ByteBuffer's position.
     *
     * @return  the next byte of data, or <code>-1</code> if the end of the
     *          stream has been reached.
     */
    public synchronized int read() {
	ensureOpen();

        try {
            // Mask off upper bits to convert to unsigned byte
            return buf.get() & 0xFF;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Reads up to <code>len</code> bytes of data into an array of bytes 
     * from this input stream. 
     * <code>-1<code> is returned when there are no more bytes in the buffer.
     * This <code>read</code> method cannot block. 
     * <P>
     * The read will update the backing ByteBuffer's position.
     *
     * @param   b     the buffer into which the data is read.
     * @param   off   the start offset of the data.
     * @param   len   the maximum number of bytes read.
     * @return  the total number of bytes read into the buffer, or
     *          <code>-1</code> if there is no more data because the end of
     *          the stream has been reached.
     */
    public synchronized int read(byte b[], int off, int len) {
	ensureOpen();

        if (available() == 0) {
            return -1;
        }

        if (len > available()) {
            len = available();
        }

        try {
            buf.get(b, off, len);
        } catch (Exception e) {
            // Should never happen
            System.err.println(this.getClass().getName() +
                ": Got exception when reading " + len + " bytes from buffer " +
                buf.toString());
            return(-2);
        }

        return(len);
    }

    /**
     * Skips <code>n</code> bytes of input from this input stream. Fewer 
     * bytes might be skipped if the end of the input stream is reached. 
     * The actual number <code>k</code>
     * of bytes to be skipped is equal to the smaller
     * of <code>n</code> and  <code>capacity-pos</code>.
     * The value <code>k</code> is added into <code>pos</code>
     * and <code>k</code> is returned.
     * <P>
     * The skip will update the backing ByteBuffer's position.
     *
     * @param   n   the number of bytes to be skipped.
     * @return  the actual number of bytes skipped.
     */
    public synchronized long skip(long n) {
	ensureOpen();

        if (n > available()) {
            n = available();
        }

        buf.position((int)(buf.position() + n));

        return n;
    }

    /**
     * Returns the number of bytes that can be read from this input 
     * stream without blocking. 
     * The value returned is
     * <code>capacity&nbsp;- pos</code>, 
     * which is the number of bytes remaining to be read from the input buffer.
     *
     * @return  the number of bytes that can be read from the input stream
     *          without blocking.
     */
    public synchronized int available() {
	ensureOpen();
	return buf.remaining();
    }

    /**
     * Tests if ByteArrayInputStream supports mark/reset.
     *
     * @since   JDK1.1
     */
    public boolean markSupported() {
	return true;
    }

    /**
     * Set the current marked position in the stream.
     * ByteBufferInputStream objects are marked at position zero by
     * default when constructed.  They may be marked at another
     * position within the buffer by this method.
     * <code>readAheadLimit</code> is ignored.
     *
     * @since   JDK1.1
     */
    public void mark(int readAheadLimit) {
	ensureOpen();

        buf.mark();
    }

    /**
     * Resets the buffer to the marked position.  The marked position
     * is the beginning unless another position was marked.
     * The value of </code>position</code> is set to 0.
     */
    public synchronized void reset() {
	ensureOpen();
        buf.rewind();
    }

    /**
     * Closes this input stream and releases any system resources 
     * associated with the stream. 
     * <p>
     */
    public synchronized void close() throws IOException {
	//isClosed = true;
	buf = null;
    }

    /** Check to make sure that the stream has not been closed */
    private void ensureOpen() {
        /* This method does nothing for now.  Once we add throws clauses
	 * to the I/O methods in this class, it will throw an IOException
	 * if the stream has been closed.
	 */
    }

    /**
     * Return the back store ByteBuffer. The returned buffer is exactly
     * the buffer that was passed to the constructor. The buffer's position
     * is the current position of the backing buffer.
     */
    public ByteBuffer getByteBuffer() {
        return buf;
    }
}
