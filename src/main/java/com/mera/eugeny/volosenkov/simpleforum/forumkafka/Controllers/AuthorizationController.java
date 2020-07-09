package com.mera.eugeny.volosenkov.simpleforum.forumkafka.Controllers;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.OutputData.SessionOut;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.Producers.SignInProducer;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.AppData;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.*;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aouthorization")
public class AuthorizationController {
    SignInProducer signInProducer;

    public AuthorizationController(SignInProducer signInProducer) {
        this.signInProducer = signInProducer;
    }

    @GetMapping("/signin")
    public QueryResult regUser(@RequestParam(value="log") String login, @RequestParam(value="pass")String password) throws InterruptedException {
        return signInProducer.send(login, password);
    }

    @GetMapping("/get_session")
    public ResultBase regUser(@RequestParam(value="queryID") Long id, @RequestParam(value="timeout") int timeout) throws InterruptedException {
        if(!AppData.resultStatusTab.containsKey(id))
        {
            return new RegUserResult(UserResultStatus.INCORRECT_QUERY_ID);
        }
        if(!AppData.resultStatusTab.get(id).isConsumed())
        {
            Thread.sleep(timeout);
        }
        if(!AppData.resultStatusTab.get(id).isConsumed())
        {
            AppData.resultStatusTab.remove(id);
            return new RegUserResult(UserResultStatus.RAISE_BY_TIMEOUT);
        }
        SessionResult res =  (SessionResult) AppData.resultStatusTab.get(id).getResult();
        AppData.resultStatusTab.remove(id);
        DataResult datares = new DataResult(new SessionOut(res.getSessionID()));
        return datares;
    }
}
