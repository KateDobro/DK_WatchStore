package org.itstep.pps2701;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.itstep.pps2701.dao.ProducerRepository;
import org.itstep.pps2701.dao.UserRepository;
import org.itstep.pps2701.dao.WatchRepository;
import org.itstep.pps2701.service.ProducerService;
import org.itstep.pps2701.service.UserService;
import org.itstep.pps2701.service.WatchService;

public class MainBindModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserRepository.class).in(Singleton.class);
        bind(WatchRepository.class).in(Singleton.class);
        bind(ProducerRepository.class).in(Singleton.class);

        bind(UserService.class).in(Singleton.class);
        bind(WatchService.class).in(Singleton.class);
        bind(ProducerService.class).in(Singleton.class);
    }
}
