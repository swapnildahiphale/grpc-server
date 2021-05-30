package com.pmm;

import Myuser.Userobject;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import io.opentracing.contrib.ServerTracingInterceptor;
import io.opentracing.util.GlobalTracer;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

public class main {


    static Properties loadConfig()
            throws IOException
    {
        String file = "tracer_config.properties";
        //if (args.length > 0)
          //  file = args[0];

        FileInputStream fs = new FileInputStream(file);
        Properties config = new Properties();
        config.load(fs);
        return config;
    }

    static boolean configureGlobalTracer(Properties config, String componentName)
            throws MalformedURLException
    {
        String tracerName = config.getProperty("tracer");
        Tracer tracer = null;
        if ("jaeger".equals(tracerName)) {
            Configuration.SamplerConfiguration samplerConfig = new Configuration.SamplerConfiguration()
                    .withType(ConstSampler.TYPE)
                    .withParam(1);
            Configuration.SenderConfiguration senderConfig = new Configuration.SenderConfiguration()
                    .withAgentHost(config.getProperty("jaeger.reporter_host"))
                    .withAgentPort(Integer.decode(config.getProperty("jaeger.reporter_port")));
            Configuration.ReporterConfiguration reporterConfig = new Configuration.ReporterConfiguration()
                    .withLogSpans(true)
                    .withFlushInterval(1000)
                    .withMaxQueueSize(10000)
                    .withSender(senderConfig);
            tracer = new Configuration(componentName).withSampler(samplerConfig).withReporter(reporterConfig).getTracer();
        } else {
            return false;
        }
        GlobalTracer.registerIfAbsent(tracer);
        return true;
    }

    public static void main(String[] res) throws Exception {

        //Properties config = loadConfig();

       // if (!configureGlobalTracer(config, "MicroDonuts"))
         //   throw new Exception("Could not configure the global tracer");

      /*  ServerTracingInterceptor tracingInterceptor = new ServerTracingInterceptor
                .Builder(tracer)
                .withStreaming()
                .withVerbosity()
                .withOperationName(new OperationNameConstructor() {
                    @Override
                    public <ReqT, RespT> String constructOperationName(MethodDescriptor<ReqT, RespT> method) {
                        // construct some operation name from the method descriptor
                    }
                })
                .withTracedAttributes(ServerTracingInterceptor.ServerRequestAttribute.HEADERS,
                        ServerTracingInterceptor.ServerRequestAttribute.METHOD_TYPE)
                .build();
*/
       Server server = ServerBuilder.forPort(10000)
                .addService(new Userobject())
                .build()
                .start();



       // Server server = ServerBuilder.forPort(10000).addService(new Userobject()).build();
       // server.start();
        System.out.println("server started at "+server.getPort());
        server.awaitTermination();
    }

}
