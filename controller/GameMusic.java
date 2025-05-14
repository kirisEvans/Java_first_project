package controller;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class GameMusic {
    ArrayList<File> musicFiles = null;
    Iterator<File> currentIterator = null;
    private Clip clip;

    public GameMusic(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();

        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(null, "找不到任何音乐文件！", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            musicFiles = new ArrayList<>(Arrays.asList(files));
            Collections.shuffle(musicFiles);
            currentIterator = musicFiles.iterator();
        }

        try {
            if (!currentIterator.hasNext()) {
                Collections.shuffle(musicFiles);
                currentIterator = musicFiles.iterator();
            }

            File next = currentIterator.next();
            AudioInputStream audio = AudioSystem.getAudioInputStream(next);

            // 获取原始格式
            AudioFormat baseFormat = audio.getFormat();

            // 创建兼容目标格式（16-bit PCM）
            AudioFormat targetFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16, // 转为 16-bit
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2, // 每帧字节数 = 通道数 * 2 (16-bit)
                    baseFormat.getSampleRate(),
                    false // 小端
            );

            // 转换流
            AudioInputStream decodedAudio = AudioSystem.getAudioInputStream(targetFormat, audio);

            clip = AudioSystem.getClip();
            clip.open(decodedAudio);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    // 如果播放到末尾才自动切换下一首
                    if (clip.getFramePosition() == clip.getFrameLength()) {
                        // 播放完当前音乐后，播放下一首
                        if (currentIterator.hasNext()) {
                            playNext();
                        } else {
                            Collections.shuffle(musicFiles);
                            currentIterator = musicFiles.iterator();
                            playNext();
                        }
                    }
                }
            });


            clip.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void playNext() {
        if (currentIterator.hasNext()) {
            try {
                File next = currentIterator.next();
                AudioInputStream audio = AudioSystem.getAudioInputStream(next);
                AudioFormat baseFormat = audio.getFormat();

                // 创建兼容目标格式（16-bit PCM）
                AudioFormat targetFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        baseFormat.getSampleRate(),
                        16, // 转为 16-bit
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2, // 每帧字节数 = 通道数 * 2 (16-bit)
                        baseFormat.getSampleRate(),
                        false // 小端
                );

                // 转换流
                AudioInputStream decodedAudio = AudioSystem.getAudioInputStream(targetFormat, audio);
                clip.stop();
                clip.flush();
                clip.close(); // 释放原资源
                clip.open(decodedAudio);
                clip.start(); // 开始播放下一首
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public Clip getClip() {
        return this.clip;
    }
}
