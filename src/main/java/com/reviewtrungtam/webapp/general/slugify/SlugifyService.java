package com.reviewtrungtam.webapp.general.slugify;

import com.github.slugify.Slugify;

public class SlugifyService {
    public static String slugify(String input) {
        Slugify slg = new Slugify();
        return slg.slugify(input);
    }
}
