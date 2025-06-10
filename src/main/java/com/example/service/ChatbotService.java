package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.theokanning.openai.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatbotService {

    private final OpenAiService openAiService;
    private final ProductRepository productRepository;

    public ChatbotService(@Value("${openai.api.key}") String apiKey, ProductRepository productRepository) {
        this.openAiService = new OpenAiService(apiKey);
        this.productRepository = productRepository;
    }

    public String askGpt(String message) {
        String intent = classifyIntent(message);

        if (message.trim().isEmpty()) {
            return "Bună! 👋 Cu ce te pot ajuta astăzi?";
        }

        if (intent.equals("multumesc")) {
            return "Eu vă mulțumesc! 💜";
        }

        if (intent.equals("irrelevant")) {
            return "Îmi pare rău, te pot ajuta doar cu întrebări legate de magazinul nostru (produse, comenzi, plată, cont, livrare).";
        }

        if (intent.equals("product")) {
            Map<String, String> keywords = extractKeywords(message.toLowerCase());

            if (keywords.isEmpty()) {
                return "Nu am înțeles exact ce cauți. Poți reformula?";
            }

            List<Product> products = productRepository.findByCategoryAndAttributes(
                    keywords.get("category"),
                    keywords.get("color"),
                    keywords.get("size")
            );

            if (products.isEmpty()) {
                return "Îmi pare rău, nu am găsit produse care să corespundă exact cerințelor tale. Poți verifica toate rochiile aici: http://localhost:3000/dresses/all";
            }

            StringBuilder productList = new StringBuilder();
            for (Product product : products) {
                if (product != null && product.getId() > 0) {
                    productList.append("- ").append(product.getName())
                            .append(" – http://localhost:3000/ProductDetails/")
                            .append(product.getId()).append("\n");
                }
            }

            String systemPrompt =
                    "Ești un consultant de modă într-un magazin online. Primești o listă de rochii deja filtrate, fiecare cu un link valid. " +
                            "Scrie un mesaj prietenos și natural în limba română în care prezinți acele produse. " +
                            "⚠️ NU adăuga nimic după link (nici punct, virgulă, paranteze etc.). " +
                            "Linkurile trebuie să apară exact așa cum le primești. " +
                            "Nu menționa informații despre livrare, parolă sau plată. Doar saluta și prezintă rochiile.";

            List<com.theokanning.openai.completion.chat.ChatMessage> messages = List.of(
                    new com.theokanning.openai.completion.chat.ChatMessage(
                            com.theokanning.openai.completion.chat.ChatMessageRole.SYSTEM.value(), systemPrompt),
                    new com.theokanning.openai.completion.chat.ChatMessage(
                            com.theokanning.openai.completion.chat.ChatMessageRole.USER.value(), "Afiseaza lista de produse:"),
                    new com.theokanning.openai.completion.chat.ChatMessage(
                            com.theokanning.openai.completion.chat.ChatMessageRole.ASSISTANT.value(), productList.toString())
            );

            var request = com.theokanning.openai.completion.chat.ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo")
                    .messages(messages)
                    .temperature(0.7)
                    .maxTokens(500)
                    .build();

            return openAiService.createChatCompletion(request)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();
        }

        if (List.of("order", "payment", "delivery", "account").contains(intent)) {
            List<com.theokanning.openai.completion.chat.ChatMessage> messages = List.of(
                    new com.theokanning.openai.completion.chat.ChatMessage(
                            com.theokanning.openai.completion.chat.ChatMessageRole.SYSTEM.value(),
                            "Ești un asistent virtual al unui magazin online de rochii. " +
                                    "Răspunde clar și politicos la întrebările despre comenzi, cont, livrare sau plată. " +
                                    "Nu inventa funcționalități. Răspunde în limba română, ca și cum ai vorbi cu un client real."),
                    new com.theokanning.openai.completion.chat.ChatMessage(
                            com.theokanning.openai.completion.chat.ChatMessageRole.USER.value(), message)
            );

            var request = com.theokanning.openai.completion.chat.ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo")
                    .messages(messages)
                    .temperature(0.7)
                    .maxTokens(300)
                    .build();

            return openAiService.createChatCompletion(request)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();
        }

        return "Îmi pare rău, nu am înțeles exact cererea. Poți reformula?";
    }

    private String classifyIntent(String input) {
        String lower = input.toLowerCase();

        if (lower.contains("multumesc") || lower.contains("mersi") || lower.contains("thanks")) {
            return "multumesc";
        }
        if (lower.contains("rochie") || lower.contains("marimea") || lower.contains("marime")
                || lower.contains("zi") || lower.contains("elegant") || lower.contains("culoare")) {
            return "product";
        }
        if (lower.contains("comanda") || lower.contains("comenzi") || lower.contains("anulare")) {
            return "order";
        }
        if (lower.contains("card") || lower.contains("plata") || lower.contains("cash") || lower.contains("ramburs")) {
            return "payment";
        }
        if (lower.contains("livrare") || lower.contains("cate zile") || lower.contains("ajunge")) {
            return "delivery";
        }
        if (lower.contains("cont") || lower.contains("parola") || lower.contains("adresa")) {
            return "account";
        }

        return "irrelevant";
    }

    private Map<String, String> extractKeywords(String input) {
        Map<String, String> keywords = new HashMap<>();
        input = input.toLowerCase();

        if (input.contains("day dress") || input.contains("day") || input.contains("zi")
                || input.contains("rochie de zi") || input.contains("rochii de zi") || input.contains("casual")) {
            keywords.put("category", "DAY DRESSES");
        } else if (input.contains("evening") || input.contains("seara") || input.contains("elegant")
                || input.contains("rochie de seara") || input.contains("rochii elegante") || input.contains("de seară")) {
            keywords.put("category", "EVENING DRESSES");
        }

        if (input.contains("alb") || input.contains("alba")) keywords.put("color", "white");
        if (input.contains("negru") || input.contains("neagra")) keywords.put("color", "black");
        if (input.contains("rosu") || input.contains("roșu") || input.contains("rosie") || input.contains("roșie")) {
            keywords.put("color", "red");
        }
        if (input.contains("galben") || input.contains("galbena")) keywords.put("color", "yellow");
        if (input.contains("roz")) keywords.put("color", "pink");
        if (input.contains("albastru") || input.contains("albastra") || input.contains("albastru deschis")) {
            keywords.put("color", "blue");
        }

        if (input.contains("s ") || input.endsWith(" s")) keywords.put("size", "S");
        if (input.contains("m ") || input.endsWith(" m")) keywords.put("size", "M");
        if (input.contains("l ") || input.endsWith(" l")) keywords.put("size", "L");

        return keywords;
    }
}
