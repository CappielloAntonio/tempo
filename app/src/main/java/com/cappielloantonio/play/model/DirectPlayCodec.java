package com.cappielloantonio.play.model;

public class DirectPlayCodec {
    public Codec codec;
    public boolean selected;

    public DirectPlayCodec(Codec codec, boolean selected) {
        this.codec = codec;
        this.selected = selected;
    }

    public enum Codec {
        FLAC("FLAC", "FLAC", "flac|flac"),
        MP3("MP3", "MP3", "mp3|mp3"),
        OPUS("Opus", "Opus", "opus|opus"),
        AAC("M4A", "AAC", "m4a|aac"),
        VORBIS("OGG", "Vorbis", "ogg|vorbis"),
        OGG("OGG", "Opus", "ogg|opus"),
        MKA("MKA", "Opus", "mka|opus");

        public final String container;
        public final String codec;
        public final String value;

        Codec(String container, String codec, String value) {
            this.container = container;
            this.codec = codec;
            this.value = value;
        }
    }
}
