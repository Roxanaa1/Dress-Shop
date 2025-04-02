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
                    keywords.get("category"), keywords.get("color"), keywords.get("size"));

            if (products.isEmpty()) {
                return "Nu am găsit produse care să corespundă cerințelor tale.";
            }

            StringBuilder response = new StringBuilder("Am găsit următoarele produse:\n");
            for (Product product : products) {
                if (product != null && product.getId() > 0) {
                    response.append("- ").append(product.getName())
                            .append(" - ").append("http://localhost:3000/ProductDetails/")
                            .append(product.getId()).append("\n");
                }
            }

            return response.toString().trim();
        }

        if (intent.equals("order")) {
            return "Poți verifica comenzile tale în contul personal, secțiunea Wishlist sau Adrese.";
        } else if (intent.equals("payment")) {
            return "Poți plăti online cu cardul sau ramburs la livrare.";
        } else if (intent.equals("delivery")) {
            return "Livrarea durează între 2-4 zile lucrătoare.";
        } else if (intent.equals("account")) {
            return "Îți poți modifica datele din secțiunea Cont -> Date personale.";
        }

        return "Îmi pare rău, nu am înțeles cererea. Poți reformula?";
    }

    private String classifyIntent(String input) {
        String lower = input.toLowerCase();

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

        if (input.contains("day dress") || input.contains("day") || input.contains("zi")
                || input.contains("rochie de zi") || input.contains("rochii de zi") || input.contains("casual")) {
            keywords.put("category", "DAY DRESSES");
        } else if (input.contains("evening") || input.contains("seara") || input.contains("elegant")
                || input.contains("rochie de seara") || input.contains("rochii elegante") || input.contains("de seară")) {
            keywords.put("category", "EVENING DRESSES");
        }

        if (input.contains("alb") || input.contains("alba")) keywords.put("color", "white");
        if (input.contains("negru") || input.contains("neagra")) keywords.put("color", "black");
        if (input.contains("roșu") || input.contains("rosu") || input.contains("roșie")) keywords.put("color", "red");

        if (input.contains("s ") || input.endsWith(" s")) keywords.put("size", "S");
        if (input.contains("m ") || input.endsWith(" m")) keywords.put("size", "M");
        if (input.contains("l ") || input.endsWith(" l")) keywords.put("size", "L");

        return keywords;
    }
}
