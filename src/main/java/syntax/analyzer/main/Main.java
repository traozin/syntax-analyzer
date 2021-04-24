package syntax.analyzer.main;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import lexical.analyzer.model.LexicalAnalyzer;
import lexical.analyzer.model.SourceCode;
import lexical.analyzer.util.FilesUtil;
import syntax.analyzer.model.SyntaxAnalyzer;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Main {

    public static void main(String[] args) {
        FilesUtil.createIfNotExists("./output");

        Pattern pattern = Pattern.compile(FilesUtil.regexInputFileFilter);
        try {
            FilesUtil.readAllFiles(Path.of("./input"), pattern)
                    .entrySet()
                    .stream()
                    .map(SourceCode::new)
                    .map(LexicalAnalyzer::new)
                    .map(LexicalAnalyzer::analyze)
                    .map(SyntaxAnalyzer::analyze)
                    .forEach(code -> {
                        Path path = Path.of(code.getPath()
                                .toString()
                                .replaceAll("input", "output")
                                .replaceAll("entrada", "saida"));
                        code.setPath(path);
                        FilesUtil.write(code);
                    });
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
