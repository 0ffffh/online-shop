package com.k0s.web;

import com.k0s.util.FileReader;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.*;
import java.util.Objects;

public class ResourcesServlet extends HttpServlet {


    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try(BufferedInputStream bufferedInputStream = new BufferedInputStream(Objects.requireNonNull(FileReader.getInputStream(req.getPathInfo())))){

            resp.setContentType(getServletContext().getMimeType(req.getRequestURI()));
            resp.setStatus(HttpServletResponse.SC_OK);
            int count;
            byte[] buffer = new byte[8192];
            while ((count = bufferedInputStream.read(buffer, 0, buffer.length)) != -1)
                resp.getOutputStream().write(buffer, 0, count);
        }
//        catch (IOException e) {
//            e.printStackTrace();
//            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
    }
}
