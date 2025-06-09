/*
 *     Copyright 2025 Philterd, LLC @ https://www.philterd.ai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ai.philterd.phinder;

import ai.philterd.phileas.model.cache.InMemoryCache;
import ai.philterd.phileas.model.configuration.PhileasConfiguration;
import ai.philterd.phileas.model.services.CacheService;
import ai.philterd.phileas.services.PhileasFilterService;
import ai.philterd.phinder.ext.PhinderParametersExtBuilder;
import org.elasticsearch.action.support.ActionFilter;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.singletonList;

public class PhinderPlugin extends Plugin implements ActionPlugin, SearchPlugin {

    @Override
    public List<ActionFilter> getActionFilters() {

        try {

            final Properties properties = new Properties();
            final PhileasConfiguration phileasConfiguration = new PhileasConfiguration(properties);
            final CacheService cacheService = new InMemoryCache();
            final PhileasFilterService phileasFilterService = new PhileasFilterService(phileasConfiguration, cacheService);

            return singletonList(new PhinderActionFilter(phileasFilterService));

        } catch (Exception ex) {
            throw new RuntimeException("Unable to initialize Phileas.", ex);
        }

    }


    @Override
    public List<SearchExtSpec<?>> getSearchExts() {

        final List<SearchExtSpec<?>> searchExts = new ArrayList<>();

        searchExts.add(
                new SearchExtSpec<>(PhinderParametersExtBuilder.PHINDER_PARAMETERS_NAME, PhinderParametersExtBuilder::new, PhinderParametersExtBuilder::parse)
        );

        return searchExts;

    }

}
