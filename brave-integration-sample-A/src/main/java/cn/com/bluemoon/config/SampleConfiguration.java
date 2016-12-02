package cn.com.bluemoon.config;

import com.github.kristofa.brave.*;
import com.twitter.zipkin.gen.Endpoint;
import org.springframework.context.annotation.*;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.Sender;
import zipkin.reporter.kafka08.KafkaSender;

/**
 * Created by leonwong on 2016/11/28.
 */
@Configuration
@Import(BraveApiConfig.class)
@EnableAspectJAutoProxy
public class SampleConfiguration {
    @Bean
    public Sender sender() {
        return KafkaSender.create("192.168.240.42:9092,192.168.240.43:9092,192.168.240.44:9092");
    }

    /**
     * Configuration for how to buffer spans into messages for Zipkin
     */
    @Bean
    public Reporter<Span> reporter() {
        return AsyncReporter.builder(sender()).build();
    }

    @Bean
    @Scope
    public Brave brave() {
        return new Brave.Builder(
                new InheritableServerClientAndLocalSpanState(Endpoint.builder().serviceName("brave-integration-sample-A").build()))
                .reporter(reporter()).build();
    }

    /**
     * 用于跟踪 in-proccess 的调用链关系
     *
     * @return
     */
    @Bean
    @Scope(value = "singleton")
    public LocalTracer localTracer() {
        return brave().localTracer();
    }
}
