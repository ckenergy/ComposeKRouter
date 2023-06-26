import com.google.devtools.ksp.symbol.KSFunctionDeclaration

/**
 * @author ckenergy
 * @date 2023/6/26
 * @desc
 */
object Utils {



}

fun KSFunctionDeclaration.getClassFullName(): Pair<String, String> {
    val packageName = containingFile!!.packageName.asString()
    val funName = simpleName.asString()
    return packageName to "${funName}Bean"
}