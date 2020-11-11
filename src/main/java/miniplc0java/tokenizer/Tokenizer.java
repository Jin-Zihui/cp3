package miniplc0java.tokenizer;

import miniplc0java.error.TokenizeError;
import miniplc0java.error.ErrorCode;
import miniplc0java.util.Pos;

public class Tokenizer {

    private StringIter it;

    public Tokenizer(StringIter it) {
        this.it = it;
    }

    // 这里本来是想实现 Iterator<Token> 的，但是 Iterator 不允许抛异常，于是就这样了
    /**
     * 获取下一个 Token
     * 
     * @return
     * @throws TokenizeError 如果解析有异常则抛出
     */
    public Token nextToken() throws TokenizeError {
        it.readAll();

        // 跳过之前的所有空白字符
        skipSpaceCharacters();

        if (it.isEOF()) {
            return new Token(TokenType.EOF, "", it.currentPos(), it.currentPos());
        }

        char peek = it.peekChar();
        if (Character.isDigit(peek)) {
            return lexUInt();
        } else if (Character.isAlphabetic(peek)) {
            return lexIdentOrKeyword();
        } else {
            return lexOperatorOrUnknown();
        }
    }

    private Token lexUInt() throws TokenizeError {
        // 请填空：
        // 直到查看下一个字符不是数字为止:
        // -- 前进一个字符，并存储这个字符
        //
        // 解析存储的字符串为无符号整数
        // 解析成功则返回无符号整数类型的token，否则返回编译错误
        //
        // Token 的 Value 应填写数字的值
        Pos startPos = it.currentPos();
        StringBuilder str = new StringBuilder();
        while(Character.isDigit(it.peekChar())) {
            str.append(it.nextChar());
        }
        try {
            int num = Integer.parseUnsignedInt(str.toString());
            Token token = new Token(TokenType.Uint, num, startPos, it.currentPos());
            return token;
        }
        catch(Exception e) {
            throw new TokenizeError(ErrorCode.InvalidInput, startPos);
        }
//        throw new Error("Not implemented");
    }

    private Token lexIdentOrKeyword() throws TokenizeError {
        // 请填空：
        // 直到查看下一个字符不是数字或字母为止:
        // -- 前进一个字符，并存储这个字符
        //
        // 尝试将存储的字符串解释为关键字
        // -- 如果是关键字，则返回关键字类型的 token
        // -- 否则，返回标识符
        //
        // Token 的 Value 应填写标识符或关键字的字符串
        Pos startPos = it.currentPos();
        StringBuilder str = new StringBuilder();
        while(Character.isDigit(it.peekChar()) || Character.isAlphabetic(it.peekChar())) {
            str.append(it.nextChar());
        }
        String strr = str.toString();
        Token token = new Token(null, strr, startPos, it.currentPos());
        switch(strr) {
            if(strr.equals("begin")){
                token.setTokenType(TokenType.Begin);
                break;
            }
            else if(strr.equals("end")){
                token.setTokenType(TokenType.End);
                break;
            }
            else if(strr.equals("const"){
                token.setTokenType(TokenType.Const);
                break;
            }
            else if(strr.equals("var"){
                token.setTokenType(TokenType.Var);
                break;
            }
            else if(strr.equals("print"){
                token.setTokenType(TokenType.Print);
                break;
            }
            else token.setTokenType(TokenType.Ident);
        }
        return token;
//        throw new Error("Not implemented");
    }

    private Token lexOperatorOrUnknown() throws TokenizeError {
        switch (it.nextChar()) {
            case '+':
                return new Token(TokenType.Plus, '+', it.previousPos(), it.currentPos());

            case '-':
                // 填入返回语句
                Token token = new Token(TokenType.Minus, '-', it.previousPos(), it.currentPos());
                return token;

            case '*':
                // 填入返回语句
                Token token =  new Token(TokenType.Mult, '*', it.previousPos(), it.currentPos());
                return token;

            case '/':
                // 填入返回语句
                Token token = new Token(TokenType.Div, '/', it.previousPos(), it.currentPos());
                return token;
                // 填入更多状态和返回语句
            case '=':
                Token token =  new Token(TokenType.Equal, '=', it.previousPos(), it.currentPos());
                return token;
            case ';':
                Token token =  new Token(TokenType.Semicolon, ';', it.previousPos(), it.currentPos());
                return token;
            case '(':
                Token token =  new Token(TokenType.LParen, '(', it.previousPos(), it.currentPos());
                return token;
            case ')':
                Token token =  new Token(TokenType.RParen, ')', it.previousPos(), it.currentPos());
                return token;
            default:
                // 不认识这个输入，摸了
                throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
        }
    }

    private void skipSpaceCharacters() {
        while (!it.isEOF() && Character.isWhitespace(it.peekChar())) {
            it.nextChar();
        }
    }
}
