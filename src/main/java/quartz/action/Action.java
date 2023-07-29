package quartz.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

@Slf4j
public class Action implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext().get("applicationContext");

            ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
            UserService userService = applicationContext.getBean(UserService.class);

            log.info("Action ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
            log.info("Stored Data {}", objectMapper.writeValueAsString(context.getJobDetail().getJobDataMap()));

            String group = context.getJobDetail().getKey().getGroup();

            log.info("Group: {}", group);

            switch (Jobs.valueOf(group)) {
                case DELETE_UNVERIFIED_USERS -> {
                    String userId = context.getJobDetail().getJobDataMap().getString("userId");
                    if (userId != null) {
                        userService.removeUnverifiedUser(userId);
                    } else {
                        log.info("userId is null");
                    }
                }
                default -> log.info("Default");
            }

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }

        log.info("Action ** {} ** completed.  Next action scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
    }
}