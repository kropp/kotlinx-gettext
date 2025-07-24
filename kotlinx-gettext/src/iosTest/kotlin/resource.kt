/*
 * Copyright 2025 Victor Kropp
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

package name.kropp.kotlinx.gettext

import kotlinx.cinterop.*
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class)
public actual fun resource(name: String): Reader {
    // In iOS tests, resources are typically bundled with the test bundle
    // We'll construct a path to the resource in the build directory similar to JS implementation
    val bundle = NSBundle.mainBundle
    val resourcePath = bundle.resourcePath + "/kotlinx-gettext/build/processedResources/ios/test/$name"
    
    // Use the iOS-specific readTextFile function to read the file
    return readTextFile(resourcePath)
}