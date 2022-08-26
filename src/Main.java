import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        try{
            HttpServer server = makeServer();
            server.start();
            initRoutes(server);
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    private static HttpServer makeServer() throws IOException {
        String host ="localhost"; // 127.0.0.1
        InetSocketAddress address = new InetSocketAddress(host, 9889);
        System.out.printf("Server is created: http://%s:%s/%n",address.getHostName(),address.getPort());
        HttpServer server = HttpServer.create(address,50);
        System.out.println(" Cool!");
        return  server;

    }
    private static void initRoutes(HttpServer server) {
        server.createContext("/", Main::handleRequest);
        server.createContext("/apps/", Main::handleRequest1);
        server.createContext("/apps/profile",Main::handleRequest2);
    }

    private static void handleRequest(HttpExchange exchange) {
        try{
            exchange.getResponseHeaders().add("Content-Type", "text/plane; charset=utf-8");

            int responseCode =200;
            int length = 0;
            exchange.sendResponseHeaders(responseCode, length);

            try (Writer writer = getWriterFrom(exchange)) {
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String ctxPath = exchange.getHttpContext().getPath();

                writeSomething(writer, "++++++++++++++++Hello World!!!++++++++++");
                write(writer, "==========HTTP Метод (и здесь можно написать что нибудь)===========", method);
                write(writer, "Запрос", uri.toString());
                write(writer, "Обработка через", ctxPath);
                writeHeaders(writer, "Заголовки запроса", exchange.getRequestHeaders());
                writeData(writer, exchange);
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Writer getWriterFrom(HttpExchange exchange) {
        OutputStream output = exchange.getResponseBody();
        Charset charset = StandardCharsets.UTF_8;
        return new PrintWriter(output, false,charset);

    }
    private static void writeHeaders(Writer writer, String type, Headers headers){
        write(writer, type, "");
        headers.forEach((k,v)-> write(writer, "\t"+k, v.toString()));
    }

    private  static  void write(Writer writer, String msg, String method){
        String data = String.format("%s: %s%n%n", msg, method);
        try {
            writer.write(data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static BufferedReader getReader (HttpExchange exchange){
        InputStream input = exchange.getRequestBody();
        Charset charset = StandardCharsets.UTF_8;
        InputStreamReader isr = new InputStreamReader(input,charset);
        return  new BufferedReader(isr);
    }

    private static void writeData(Writer writer,  HttpExchange exchange) {
        try (BufferedReader reader = getReader(exchange)) {
            if (!reader.ready()) {
                return;
            }
            write(writer, "Блок данных", "");
            reader.lines().forEach(v -> write(writer, "\t", v));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest1(HttpExchange exchange) {
        try{
            exchange.getResponseHeaders().add("Content-Type", "text/plane; charset=utf-8");
            int responseCode =200;
            int length = 0;
            exchange.sendResponseHeaders(responseCode, length);

            try (Writer writer = getWriterFrom(exchange)) {
                URI uri = exchange.getRequestURI();

                writeSomething (writer, "Привет МИР!!!");
                write(writer, "Тут должен быть запрос....:::", uri.toString());
                writeSomething(writer, "================");
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static  void writeSomething (Writer writer, String msg){
        String data = String.format("%s%n%n", msg);
        try {
            writer.write(data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void handleRequest2(HttpExchange exchange) {
        try{
            exchange.getResponseHeaders().add("Content-Type", "text/plane; charset=utf-8");
            int responseCode =200;
            int length = 0;
            exchange.sendResponseHeaders(responseCode, length);

            try (Writer writer = getWriterFrom(exchange)) {

                writeSomething (writer, "==========********+==========");
                writeSomething (writer, "Привет Привет JAVA devoloper!!!");
                writeSomething(writer, "==========************==========");
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}

