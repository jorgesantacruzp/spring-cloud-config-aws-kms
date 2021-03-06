/**
 * Copyright (C) 2015 Zalando SE (https://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.zalando.spring.cloud.config.aws.kms;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.ByteBuffer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@ConditionalOnProperty(prefix = "aws.kms", name = "useMock", havingValue = "true")
@AutoConfigureBefore(KmsEncryptionConfiguration.class)
class MockAwsKmsConfig {

    @Bean
    AWSKMS kms() {
        final AWSKMS mock = mock(AWSKMS.class);
        when(mock.decrypt(any(DecryptRequest.class))).thenAnswer(new Answer<DecryptResult>() {
            @Override
            public DecryptResult answer(InvocationOnMock invocation) throws Throwable {
                return new DecryptResult().withPlaintext(ByteBuffer.wrap(KmsEncryptionIntegrationConfigurationTest.PLAINTEXT.getBytes()));
            }
        });
        return mock;
    }

}
