package com.narij.checkv2.library;

import android.widget.ImageView;

import com.arnahit.chipinputlayout.Chip;
import com.arnahit.chipinputlayout.ChipImageRenderer;
import com.arnahit.chipinputlayout.LetterTileProvider;
import com.bumptech.glide.Glide;

public class GlideRenderer implements ChipImageRenderer {
    @Override
    public void renderAvatar(ImageView imageView, Chip chip) {
        if (chip.getAvatarUri() != null) {
            // Use Glide to load URL (provided in avatar uri)
            Glide.with(imageView.getContext())
                    .load(chip.getAvatarUri())
                    .into(imageView);
        } else {
            // Default to circular tile if no uri exists
            imageView.setImageBitmap(LetterTileProvider
                    .getInstance(imageView.getContext())
                    .getLetterTile(chip.getTitle()));
        }
    }
}