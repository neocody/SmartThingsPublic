import org.codehaus.groovy.control.SourceUnit

// Parse grammar only: no semantic analysis, AST transforms or script execution.
def files = []
['smartapps', 'devicetypes'].each { root ->
    new File(root).eachFileRecurse { file ->
        if (file.isFile() && file.name.endsWith('.groovy')) files << file
    }
}
if (files.isEmpty()) throw new IllegalStateException('No SmartThings sources found')
files.sort { it.path }.each { file ->
    SourceUnit.create(file.path, file.getText('UTF-8')).parse()
}
println "Parsed ${files.size()} Groovy source files"
