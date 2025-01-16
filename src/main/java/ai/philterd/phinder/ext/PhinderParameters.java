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
package ai.philterd.phinder.ext;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.search.SearchExtBuilder;
import org.elasticsearch.xcontent.ObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * The Phinder parameters available in the ext.
 */
public class PhinderParameters implements Writeable, ToXContentObject {

    private static final ObjectParser<PhinderParameters, Void> PARSER;
    private static final ParseField POLICY = new ParseField("policy");
    private static final ParseField CONTEXT = new ParseField("context");
    private static final ParseField FIELD_NAME = new ParseField("field");

    static {
        PARSER = new ObjectParser<>(PhinderParametersExtBuilder.PHINDER_PARAMETERS_NAME, PhinderParameters::new);
        PARSER.declareString(PhinderParameters::setPolicy, POLICY);
        PARSER.declareString(PhinderParameters::setContext, CONTEXT);
        PARSER.declareString(PhinderParameters::setFieldName, FIELD_NAME);
    }

    /**
     * Get the {@link PhinderParameters} from a {@link SearchRequest}.
     * @param request A {@link SearchRequest},
     * @return The Phinder {@link PhinderParameters parameters}.
     */
    public static PhinderParameters getPhinderParameters(final SearchRequest request) {

        PhinderParametersExtBuilder builder = null;

        if (request.source() != null && request.source().ext() != null && !request.source().ext().isEmpty()) {
            final Optional<SearchExtBuilder> b = request.source()
                    .ext()
                    .stream()
                    .filter(bldr -> PhinderParametersExtBuilder.PHINDER_PARAMETERS_NAME.equals(bldr.getWriteableName()))
                    .findFirst();
            if (b.isPresent()) {
                builder = (PhinderParametersExtBuilder) b.get();
            }
        }

        if (builder != null) {
            return builder.getParams();
        } else {
            return null;
        }

    }

    private String policy;
    private String context;
    private String fieldName;

    /**
     * Creates a new instance.
     */
    public PhinderParameters() {}

    /**
     * Creates a new instance.
     * @param input The {@link StreamInput} to read parameters from.
     * @throws IOException Thrown if the parameters cannot be read.
     */
    public PhinderParameters(StreamInput input) throws IOException {
        this.policy = input.readString();
        this.context = input.readString();
        this.fieldName = input.readString();
    }

    /**
     * Creates a new instance.
     * @param policy The name of the policy to apply.
     */
    public PhinderParameters(String policy) {
        this.policy = policy;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder xContentBuilder, ToXContent.Params params) throws IOException {
        return xContentBuilder
                .field(POLICY.getPreferredName(), this.policy)
                .field(CONTEXT.getPreferredName(), this.context)
                .field(FIELD_NAME.getPreferredName(), this.fieldName);
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(policy);
        out.writeString(context);
        out.writeString(fieldName);
    }

    /**
     * Create the {@link PhinderParameters} from a {@link XContentParser}.
     * @param parser An {@link XContentParser}.
     * @return The {@link PhinderParameters}.
     * @throws IOException Thrown if the parameters cannot be read.
     */
    public static PhinderParameters parse(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final PhinderParameters other = (PhinderParameters) o;
        return Objects.equals(this.policy, other.getPolicy());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), this.policy);
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

}