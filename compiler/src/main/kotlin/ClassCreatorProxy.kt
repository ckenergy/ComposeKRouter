import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.*

/**
 * ckenergy  2022-11-11
 */
class ClassCreatorProxy(val logBuilder: StringBuilder) {

    private val moduleMemberName = MemberName("com.ckenergy.compose.plugin.core", "composeModules")

    /**
     * 创建Java代码
     *
     * @return
     */
    fun generateJavaCode(
        packageName: String,
        name: String,
        routerList: List<KSFunctionDeclaration>
    ): FileSpec {
        val moduleClassName = ClassName("com.ckenergy.compose.plugin.core", "ModuleBuilder") //This will take care of the import

        val codeBlockBuilder =
            CodeBlock.Builder()
                .beginControlFlow("%M", moduleMemberName)//This will take care of the {} and indentations
                .addStatement("packageName = \"$packageName\"")
        routerList.forEach {
            val parent = it
            val composePackageName = parent.containingFile!!.packageName.asString()
            val funName = parent.simpleName.asString()
            val routeName =
                parent.annotations.firstOrNull { it1 ->
                    it1.annotationType.resolve().declaration.qualifiedName?.asString() == Constants.KEY_KROUTER_NAME
                }?.arguments?.find { it1 -> it1.name?.getShortName() == "routeName" }?.value.toString()

            val composeFunName = MemberName(composePackageName, funName)

            codeBlockBuilder.addStatement(
                "composable(\"$routeName\") {" +
                        "%M()" +
                        "}", composeFunName)
        }
        codeBlockBuilder.endControlFlow()

        val module = PropertySpec.builder(name, moduleClassName)
            .initializer(codeBlockBuilder.build())
            .build()

        return FileSpec.builder(packageName, name)
            .addProperty(module)
            .build()
    }

    private fun log(msg: String) {
        logBuilder.append(msg + "\n")
    }

}