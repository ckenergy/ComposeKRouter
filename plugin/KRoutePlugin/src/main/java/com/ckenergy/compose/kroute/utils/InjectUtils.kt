package com.ckenergy.compose.kroute.utils

import com.ckenergy.compose.kroute.RegisterKRouteClassVisitor
import org.objectweb.asm.*
import org.objectweb.asm.commons.AdviceAdapter
import java.io.InputStream

/**
 * generate register code into LogisticsCenter.class
 */
object InjectUtils {
    // refer hack class when object init
    fun referHackWhenInit(inputStream: InputStream, targetList: List<ScanSetting>): ByteArray {
        val cr = ClassReader(inputStream)
        val cw = ClassWriter(cr, ClassWriter.COMPUTE_FRAMES)
        val cv = RegisterKRouteClassVisitor(ScanSetting.ASM_API, cw, targetList.first().classList.toSet())
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        return cw.toByteArray()
    }
}
