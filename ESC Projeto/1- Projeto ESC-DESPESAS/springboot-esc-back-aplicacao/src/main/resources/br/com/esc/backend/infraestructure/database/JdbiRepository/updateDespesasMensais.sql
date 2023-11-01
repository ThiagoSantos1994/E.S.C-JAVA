UPDATE
    tbd_DespesaMensal
SET
    ds_NomeDespesa = :mensal.dsNomeDespesa,
    vl_Limite = :mensal.vlLimite,
    id_OrdemExibicao = :mensal.idOrdemExibicao,
    id_Emprestimo = :mensal.idEmprestimo,
    tp_Reprocessar = :mensal.tpReprocessar,
    tp_Emprestimo = :mensal.tpEmprestimo,
    tp_Poupanca = :mensal.tpPoupanca,
    tp_Anotacao = :mensal.tpAnotacao,
    tp_DebitoAutomatico = :mensal.tpDebitoAutomatico,
    tp_Meta = :mensal.tpMeta,
    tp_LinhaSeparacao = :mensal.tpLinhaSeparacao,
    tp_DespesaReversa = :mensal.tpDespesaReversa,
    tp_PoupancaNegativa = :mensal.tpPoupancaNegativa,
    tp_Relatorio = :mensal.tpRelatorio,
    tp_DebitoCartao = :mensal.tpDebitoCartao,
    tp_EmprestimoAPagar = :mensal.tpEmprestimoAPagar,
    tp_ReferenciaSaldoMesAnterior = :mensal.tpReferenciaSaldoMesAnterior,
    tp_VisualizacaoTemp = :mensal.tpVisualizacaoTemp,
    tp_DespesaCompartilhada = :mensal.tpDespesaCompartilhada
WHERE
   id_Despesa = :mensal.idDespesa
   AND id_DetalheDespesa = :mensal.idDetalheDespesa
   AND id_Funcionario = :mensal.idFuncionario



