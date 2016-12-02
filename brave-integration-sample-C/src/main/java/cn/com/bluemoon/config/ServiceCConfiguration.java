package cn.com.bluemoon.config;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.BraveApiConfig;
import com.github.kristofa.brave.InheritableServerClientAndLocalSpanState;
import com.github.kristofa.brave.LocalTracer;
import com.twitter.zipkin.gen.Endpoint;
import org.springframework.context.annotation.*;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.Sender;
import zipkin.reporter.kafka08.KafkaSender;

/**
 * Created by leonwong on 2016/12/2.
 */
@Configuration
@Import(BraveApiConfig.class)
@EnableAspectJAutoProxy
public class ServiceCConfiguration {
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

    /**
     * 支持嵌套调用，但是如果嵌套过多的情况，有可能会出现内存溢出的风险
     *
     * @return
     * @see InheritableServerClientAndLocalSpanState
     */
    @Bean
    @Scope
    public Brave brave() {
        return new Brave.Builder(
                new InheritableServerClientAndLocalSpanState(Endpoint.builder().serviceName("brave-integration-sample-C").build()))
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
