UPDATE
   tbd_Parcelas
SET
    vl_Parcela = :parcela.vlParcela,
    vl_Desconto = :parcela.vlDesconto,
    ds_Observacoes = :parcela.dsObservacoes,
    tp_Baixado = :parcela.tpBaixado,
    tp_Quitado = :parcela.tpQuitado,
    tp_ParcelaAdiada = :parcela.tpParcelaAdiada,
    tp_ParcelaAmortizada = :parcela.tpParcelaAmortizada
WHERE
   id_DespesaParcelada = :parcela.idDespesaParcelada
   AND id_Parcelas = :parcela.idParcela
   AND id_Funcionario = :parcela.idFuncionario
