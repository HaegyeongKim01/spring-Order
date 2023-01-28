//특정한 환경변수, 시스템 속성, application에 필요한 속성을 제공해주는 목적을 가지는 class를 만들어서  property source를 define할 수 있다.
package org.progms.kdt.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("version.properties")
public class VersionProvider {
    private final String version;

    public VersionProvider(@Value("${version}:v0.0.0}") String version){
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
