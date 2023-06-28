@file:Suppress("SpellCheckingInspection")

package com.ckenergy.compose.kroute.utils

import org.objectweb.asm.Opcodes

/**
 * register setting
 */
class ScanSetting(_interfaceName: String) {
    val interfaceName: String

    init {
        interfaceName = "com/ckenergy/compose/plugin/core/$_interfaceName"
    }

    /**
     * scan result for {@link #interfaceName}
     * class names in this list
     */
    val classList = mutableListOf<String>()

    companion object {

        const val ASM_API = Opcodes.ASM9

        /**
         * The register code is generated into this class
         */
        const val GENERATE_TO_CLASS_NAME = "com/ckenergy/compose/plugin/core/NavGraphManager"

        /**
         * you know. this is the class file(or entry in jar file) name
         */
        const val GENERATE_TO_CLASS_FILE_NAME = "$GENERATE_TO_CLASS_NAME.class"

        /**
         * The register code is generated into this method
         */
        const val GENERATE_TO_METHOD_NAME = "registerMap"

        /**
         * register method name in class: {@link #GENERATE_TO_CLASS_NAME}
         */
        const val REGISTER_METHOD_NAME = "register"
    }

}
