/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.core.form.internal;

import com.google.common.reflect.TypeToken;
import ratpack.core.form.Form;
import ratpack.core.form.FormParseOpts;
import ratpack.core.handling.Context;
import ratpack.core.http.TypedData;
import ratpack.core.parse.Parse;
import ratpack.core.parse.Parser;
import ratpack.core.parse.ParserSupport;
import ratpack.func.MultiValueMap;
import ratpack.func.Types;

import static ratpack.func.MultiValueMap.empty;

public class FormParser extends ParserSupport<FormParseOpts> {

  private static final TypeToken<Form> FORM_TYPE = Types.token(Form.class);

  public static final TypeToken<Parser<FormParseOpts>> TYPE = Types.intern(new TypeToken<Parser<FormParseOpts>>() {});
  public static final Parser<FormParseOpts> INSTANCE = new FormParser();

  private FormParser() {
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T parse(Context context, TypedData requestBody, Parse<T, FormParseOpts> parse) throws Exception {
    if (parse.getType().equals(FORM_TYPE)) {
      MultiValueMap<String, String> base = parse.getOpts().map(FormParseOpts::isIncludeQueryParams).orElse(false)
        ? context.getRequest().getQueryParams()
        : empty();
      return Types.cast(FormDecoder.parseForm(context, requestBody, base));
    } else {
      return null;
    }
  }

}
