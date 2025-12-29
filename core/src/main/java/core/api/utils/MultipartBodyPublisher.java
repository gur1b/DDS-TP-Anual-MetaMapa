package core.api.utils;

import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MultipartBodyPublisher {

    private static final String CRLF = "\r\n";

    private final String boundary;
    private final List<byte[]> parts = new ArrayList<>();

    public MultipartBodyPublisher() {
        this.boundary = UUID.randomUUID().toString();
    }

    public String getBoundary() {
        return boundary;
    }

    public MultipartBodyPublisher addFilePart(String name,
                                              String filename,
                                              String contentType,
                                              byte[] content) {

        // Header
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append(CRLF);
        sb.append("Content-Disposition: form-data; name=\"")
                .append(name).append("\"; filename=\"")
                .append(filename).append("\"").append(CRLF);
        sb.append("Content-Type: ").append(contentType).append(CRLF);
        sb.append(CRLF);

        byte[] headerBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[] endBytes = CRLF.getBytes(StandardCharsets.UTF_8);

        // Unimos header + contenido + salto final
        byte[] part = new byte[headerBytes.length + content.length + endBytes.length];
        System.arraycopy(headerBytes, 0, part, 0, headerBytes.length);
        System.arraycopy(content, 0, part, headerBytes.length, content.length);
        System.arraycopy(endBytes, 0, part, headerBytes.length + content.length, endBytes.length);

        parts.add(part);
        return this;
    }

    public HttpRequest.BodyPublisher build() {

        // closing boundary: "--boundary--\r\n"
        String closing = "--" + boundary + "--" + CRLF;
        parts.add(closing.getBytes(StandardCharsets.UTF_8));

        return HttpRequest.BodyPublishers.ofByteArrays(parts);
    }
}

