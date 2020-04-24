# WebFlux (Programação Reativa)

# Não Finalizado

O que é programação reativa?

23.1.1 O que é programação reativa?
Em termos simples, a programação reativa é sobre aplicativos não-bloqueadores que são assíncronos e controlados por eventos e requerem um pequeno número de encadeamentos para dimensionar verticalmente (por exemplo, dentro da JVM) e não horizontalmente (por meio de cluster).

Um aspecto fundamental das aplicações reativas é o conceito de contrapressão, que é um mecanismo para garantir que os produtores não sobrecarreguem os consumidores. Por exemplo, em um pipeline de componentes reativos que se estendem do banco de dados para a resposta HTTP quando a conexão HTTP é muito lenta, o repositório de dados também pode ficar mais lento ou parar completamente até que a capacidade da rede seja liberada.

A programação reativa também leva a uma grande mudança da composição da lógica imperativa para a declarativa assíncrona. É comparável a escrever código de bloqueio versus usar o CompletableFuturedo Java 8 para compor ações de acompanhamento por meio de expressões lambda.

Para uma introdução mais longa, consulte a série de blogs "Notes on Reactive Programming", de Dave Syer.



Contrapressão

 No contexto reativo, conceitos como Streaming e Backpressure (contrapressão) compõem a base para compreendermos aspectos essenciais do paradigama reativo. O conceito de Streaming é apresentado como uma eficiente técnica arquitetural a qual explora modelos de construção de fluxos de dados, reagem a eventos ocorridos na linha do tempo, tendo seu processamento e suas transformações de dados realizadas em um Pipeline composto de Steps (passos) conhecidos como Processors (processadores). O conceito de Backpressure (contrapressão) refere-se a um mecanismo para gerenciamento de carga entre os Steps do pipeline em relação a demanda de chamadas aos Upstream Services (Publishers), impedindo sobrecarga entre os componentes (Subscribers).
