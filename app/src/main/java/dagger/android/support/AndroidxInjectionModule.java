/*
 * Copyright (C) 2017 The Dagger Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dagger.android.support;

import java.util.*;

import androidx.fragment.app.*;
import dagger.*;
import dagger.android.*;
import dagger.internal.*;
import dagger.multibindings.*;

/**
 * Configures bindings to ensure the usability of {@code dagger.android} and {@code
 * dagger.android.support} framework classes. This module should be installed in the root-most
 * component which will use these types.
 */
@Beta
@Module(includes = AndroidInjectionModule.class)
public abstract class AndroidxInjectionModule {
    @Multibinds
    abstract Map<Class<? extends Fragment>, AndroidInjector.Factory<? extends Fragment>>
    androidxFragmentInjectorFactories();

    @Multibinds
    abstract Map<String, AndroidInjector.Factory<? extends Fragment>>
    androidxFragmentInjectorFactoriesWithStringKeys();

    private AndroidxInjectionModule() {
    }
}
