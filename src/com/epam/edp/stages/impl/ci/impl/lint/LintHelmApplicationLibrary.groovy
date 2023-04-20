/* Copyright 2022 EPAM Systems.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.*/

package com.epam.edp.stages.impl.ci.impl.lint

import org.apache.commons.lang.RandomStringUtils
import com.epam.edp.stages.impl.ci.ProjectType
import com.epam.edp.stages.impl.ci.Stage

@Stage(name = "helm-lint", buildTool = "any", type = [ProjectType.APPLICATION, ProjectType.LIBRARY])
class LintHelmApplicationLibrary {
    Script script

    void run(context) {
        def test_dir = "${RandomStringUtils.random(10, true, true)}"
        def filesToStash =  "deploy-templates/**,ct.yaml"
        script.dir("${context.workDir}") {
            script.stash name: 'deploy-templates-data', includes: "${filesToStash}", useDefaultExcludes: false
        }
        script.node("edp-helm") {
            script.dir("${test_dir}") {
                script.unstash 'deploy-templates-data'
                        script.sh (script: """
                               set -ex
                               ct lint --charts deploy-templates/
                        """)
            }
        }
    }
}

