package ceng.estu.edu;

import java.util.Map;
import java.util.Random;

class Node extends Thread {

    @Override
    public void run() {

        int random = new Random().nextInt(2000);
        try {
            Thread.sleep(random);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void perform() {

        switch (this.getState()) {
            case NEW, RUNNABLE -> {

                if (!Main.started.contains(this)) {

                    boolean flag = true;
                    String causes = "";

                    for (Map.Entry<String, String> entry : Main.map.entrySet()) {
                        if (entry.getValue().equals(this.getName())) {
                            flag = false;
                            causes = entry.getKey();

                            String[] split = entry.getKey().split(",");
                            for (String s : split) {
                                Node temp = Main.findNode(s, Main.threads);
                                if (temp.getState() == Thread.State.TERMINATED) {
                                    if (!Main.terminated.contains(temp)) {
                                        Main.terminated.add(temp);
                                        Main.started.remove(temp);
                                        Main.waiting.remove(temp);
                                        temp.printToConsole("");
                                    }
                                    flag = true;
                                } else {
                                    flag = false;
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    if (flag) {
                        if (!Main.started.contains(this)) {
                            this.printToConsole("s");
                            Main.started.add(this);
                            this.start();
                            Main.waiting.remove(this);
                        }
                    } else {
                        if (!Main.waiting.contains(this)) {
                            this.printToConsole(causes);
                            Main.waiting.add(this);
                        }
                    }
                }
            }
            case TERMINATED -> {
                if (!Main.terminated.contains(this)) {
                    Main.terminated.add(this);
                    Main.waiting.remove(this);
                    Main.started.remove(this);
                    this.printToConsole("");
                }
                if (Main.terminated.size() == Main.threads.size()) {
                    System.out.println("All threads are terminated");
                    System.exit(0);
                }

            }

        }

    }

    public void printToConsole(String causes) {

        if (this.getState() == Thread.State.TERMINATED) {
            System.out.println("Node" + this.getName() + " is completed");
        } else {
            if (causes.equals("s")) {
                System.out.println("Node" + this.getName() + " is being started");
            } else {
                System.out.println("Node" + this.getName() + " is waiting for " + causes);
            }
        }
    }

}