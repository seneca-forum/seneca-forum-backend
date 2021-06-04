package com.seneca.senecaforum.service.utils;

import com.seneca.senecaforum.domain.Topic;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class TopicIdPrefixed extends SequenceStyleGenerator {
    private String format;
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String topicName = ((Topic)object).getTopicName().replace(" ","_").replace("/","");
        return String.format(format,topicName,super.generate(session,object));
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        super.configure(LongType.INSTANCE, params, serviceRegistry);
        this.format = "%1$s";

    }
}
