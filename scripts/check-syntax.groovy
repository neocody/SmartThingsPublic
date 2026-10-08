import org.codehaus.groovy.control.SourceUnit

// Parse and build the syntax tree: no semantic analysis, AST transforms or execution.
def files = []
['smartapps', 'devicetypes'].each { root ->
    new File(root).eachFileRecurse { file ->
        if (file.isFile() && file.name.endsWith('.groovy')) files << file
    }
}
if (files.isEmpty()) throw new IllegalStateException('No SmartThings sources found')
files.sort { it.path }.each { file ->
    def unit = SourceUnit.create(file.path, file.getText('UTF-8'))
    unit.parse()
    unit.nextPhase()
    unit.convert()
    if (unit.getErrorCollector().hasErrors()) {
        throw new IllegalStateException("Syntax errors in ${file.path}")
    }
}
println "Parsed ${files.size()} Groovy source files"
