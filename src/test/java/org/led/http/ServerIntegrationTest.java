package org.led.http;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.led.simple.SimpleRequestHandler;
import org.led.util.Util;

public class ServerIntegrationTest {

    private static Server server;

    @BeforeClass
    public static void setup() throws Exception {
        Thread serverThread = new Thread(new Runnable() {

            @Override
            public void run() {
                server = new Server(new ServerConfiguration(8889, 10, 10,
                        new SimpleRequestHandler()));
                try {
                    server.start();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }
        });
        serverThread.start();

        while (server == null || !server.isStarted()) {
            // wait until server is started
            Thread.sleep(1000);
        }

    }

    @AfterClass
    public static void after() {
        server.stop();
    }

    @Test
    public void shouldProcessGET() throws Exception {

        // setup

        // act
        HttpGet get = new HttpGet("http://localhost:8889/LICENSE-2.0.txt");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);

        // assert
        System.err.println("RESPONSE: " + response.toString());

    }

    @Test
    public void shouldProcessSeveralGets() throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://localhost:8889/test");
        for (int i = 0; i < 10; i++) {
            HttpResponse response = client.execute(get);
            EntityUtils.consumeQuietly(response.getEntity());

        }

    }

    @Test
    public void shouldProcessConcurrentGets() throws Exception {

        int threads = 300;
        final HttpGet get = new HttpGet("http://localhost:8889/test");
        final CyclicBarrier barrier = new CyclicBarrier(threads);
        final AtomicInteger done = new AtomicInteger();
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threads; i++) {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        final DefaultHttpClient client = new DefaultHttpClient();
                        barrier.await();
                        HttpResponse response = client.execute(get);

                        EntityUtils.consumeQuietly(response.getEntity());
                        assertEquals(200, response.getStatusLine()
                                .getStatusCode());
                        done.incrementAndGet();
                    } catch (InterruptedException | BrokenBarrierException
                            | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        while (done.get() != threads) {
            // wait
            Thread.sleep(500);

        }
        System.err.println("DONE, Time: "
                + (System.currentTimeMillis() - startTime));

    }

    @Test
    public void shouldProcessPostWithBody() throws Exception {
        // setup

        // act
        HttpPost post = new HttpPost("http://localhost:8889/test");
        post.addHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity("{\"key\":\"value\"}");
        post.setEntity(entity);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);

        // assert
        System.err.println("RESPONSE: " + response.toString());
        assertEquals(200, response.getStatusLine().getStatusCode());
        System.err.println("\n: " + Util.bytesToHex("\n".getBytes()));
    }
}
