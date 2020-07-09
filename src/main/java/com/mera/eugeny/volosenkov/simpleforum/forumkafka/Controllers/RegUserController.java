package com.mera.eugeny.volosenkov.simpleforum.forumkafka.Controllers;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.Producers.RegUserProducer;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.AppData;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.QueryResult;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.RegUserResult;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.UserResultStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reg_user")
public class RegUserController {
    public RegUserController(RegUserProducer checkUserPr) {
        this.regUserPr = checkUserPr;
    }

    RegUserProducer regUserPr;
    @GetMapping("/reg")
    public QueryResult regUser(@RequestParam(value="log") String login, @RequestParam(value="pass")String password) throws InterruptedException {
        return regUserPr.sendMessage(login, password);
    }

    @GetMapping("/reg_result")
    public RegUserResult regUser(@RequestParam(value="queryID") Long id, @RequestParam(value="timeout") int timeout) throws InterruptedException {
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
        RegUserResult res =  (RegUserResult) AppData.resultStatusTab.get(id).getResult();
        AppData.resultStatusTab.remove(id);
        return res;
    }
}
