package io.topi.apptopi;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.topi.apptopi.model.GitItem;
import io.topi.apptopi.service.GitService;

import static org.junit.Assert.fail;

public class GetListTest {

    @Test
    public void getList(){
        final CountDownLatch latch = new CountDownLatch(1);
        GitService.getGitApi("language:Java", "stars", "1", new GitService.GitApiCallback() {
            @Override
            public void onSucess(List<GitItem> gitItems) {
                latch.countDown();
            }

            @Override
            public void onNotConnection() {
                fail("FALHA NA CONEXÃO");
                latch.countDown();
            }
        });
        try{
            latch.await();
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}
